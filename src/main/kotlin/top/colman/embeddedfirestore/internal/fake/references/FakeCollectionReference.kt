/*
 *    Copyright 2020 Leonardo Colman Lopes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package top.colman.embeddedfirestore.internal.fake.references

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.isPublic
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.from
import top.colman.embeddedfirestore.internal.fake.future.InstantApiFuture
import top.colman.embeddedfirestore.internal.fake.notFrom
import top.colman.embeddedfirestore.internal.fake.notNamed

@Suppress("TooManyFunctions")
internal class FakeCollectionReference(
    private val firestore: Firestore,
    private val path: String,
    private val collectionId: String
) {
    
    private val documents: MutableMap<String, FakeDocumentReference> = mutableMapOf()

    // My methods
    
    fun delete(id: String) {
        documents.remove(id)
    }
    
    fun setDocument(id: String, fakeDocumentReference: FakeDocumentReference) {
        documents[id] = fakeDocumentReference
    }
        
        
    // CollectionReference methods
    
    fun getId(): String = collectionId
    
    fun getParent(): DocumentReference = TODO()
    
    fun getPath(): String = path
    
    fun document(): DocumentReference = FakeDocumentReference(mutableMapOf(), this).asDocumentReference()
    
    fun document(childPath: String): DocumentReference = 
        FakeDocumentReference(
            mutableMapOf(),
            this,
            childPath.substringAfterLast("/"),
            childPath
        ).asDocumentReference()
    
    fun listDocuments(): Iterable<DocumentReference> = documents.values.map { it.asDocumentReference() }
    
    fun add(fields: Map<String, Any>): ApiFuture<DocumentReference> {
        val reference = FakeDocumentReference(fields.toMutableMap(), this)
        documents[reference.id] = reference
        return InstantApiFuture(reference.asDocumentReference())
    }
    
    fun add(pojo: Any): ApiFuture<DocumentReference> = TODO()
    
    fun getFirestore(): Firestore = firestore
    
    
    /**
     * This method is necessary because the construction of a CollectionReference is not possible
     * via simple interfaces or instantiation, and thus we need to create some proxies to delegate
     * to our fake implementation.
     * 
     * All methods from [CollectionReference] are implemented, except for [CollectionReference::getResourcePath], as 
     * it's package private and used only inside [CollectionReference].
     * Methods from [Any] are also excluded. Methods from [Query] are delegated to [FakeQuery]
     */
    fun asCollectionReference(): CollectionReference {
        return ByteBuddy()
            .subclass(CollectionReference::class.java)
            .method(
                notFrom<Any>().and(notFrom<Query>()).and(notNamed("getResourcePath"))
            )
            .intercept(MethodDelegation.to(this))
            .method(
                from<Query>()
                    .and(notNamed("getFirestore"))
                    .and(isPublic<MethodDescription>())
            )
            .intercept(MethodDelegation.to(FakeQuery(this.documents.values.toList())))
            .make()
            .load(CollectionReference::class.java.classLoader)
            .loaded.createInstance()
    }
}
