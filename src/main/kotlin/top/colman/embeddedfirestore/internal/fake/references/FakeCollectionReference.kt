/*
 *    Copyright 2019 Leonardo Colman Lopes
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
import com.google.cloud.firestore.Query
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.notFrom
import top.colman.embeddedfirestore.internal.fake.notNamed


internal class FakeCollectionReference(
    private val collectionId: String
) {
    
    private val documents: MutableList<Map<String, Any>> = mutableListOf()
    
    fun getId(): String = collectionId
    
    fun getParent(): DocumentReference = TODO()
    
    fun getPath(): String = TODO()
    
    fun document(): DocumentReference = TODO()
    
    fun document(childPath: String): DocumentReference = TODO()
    
    fun listDocuments(): Iterable<DocumentReference> = TODO()
    
    fun add(fields: Map<String, Any>): ApiFuture<DocumentReference> {
        documents.add(fields)
        
    }
    
    fun add(pojo: Any): ApiFuture<DocumentReference> = TODO()
    
    
    /**
     * This method is necessary because the construction of a CollectionReference is not possible
     * via simple interfaces or instantiation, and thus we need to create some proxies to delegate
     * to our fake implementation.
     * 
     * All methods from [CollectionReference] are implemented, except for [CollectionReference::getResourcePath], as it's
     * package private and used only inside [CollectionReference].
     * Methods from [Any] and [Query] are also excluded.
     */
    fun asCollectionReference(): CollectionReference {
        return ByteBuddy()
            .subclass(CollectionReference::class.java)
            .method(
                notFrom<Any>().and(notFrom<Query>()).and(notNamed("getResourcePath"))
            )
            .intercept(MethodDelegation.to(this))
            .make()
            .load(CollectionReference::class.java.classLoader)
            .loaded.createInstance()
    }
}