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

package top.colman.embeddedfirestore.internal

import com.google.api.core.ApiFuture
import com.google.api.gax.rpc.ApiStreamObserver
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.FieldMask
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Transaction.Function
import com.google.cloud.firestore.TransactionOptions
import com.google.cloud.firestore.WriteBatch
import top.colman.embeddedfirestore.FirestoreInitialization
import top.colman.embeddedfirestore.internal.fake.references.FakeCollectionReference

internal class FakeFirestore : Firestore, FirestoreInitialization {
    
    private val collectionList = mutableListOf<FakeCollectionReference>()
    
    override fun collection(path: String): CollectionReference = collectionList.first { it.getPath() == path }.asCollectionReference()
    
    override fun getAll(vararg documentReferences: DocumentReference?): ApiFuture<MutableList<DocumentSnapshot>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getAll(
        documentReferences: Array<out DocumentReference>,
        fieldMask: FieldMask?
    ): ApiFuture<MutableList<DocumentSnapshot>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getAll(
        documentReferences: Array<out DocumentReference>,
        fieldMask: FieldMask?,
        responseObserver: ApiStreamObserver<DocumentSnapshot>?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun collectionGroup(collectionId: String): Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun <T : Any?> runTransaction(updateFunction: Function<T>): ApiFuture<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun <T : Any?> runTransaction(
        updateFunction: Function<T>,
        transactionOptions: TransactionOptions
    ): ApiFuture<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun batch(): WriteBatch {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun listCollections(): Iterable<CollectionReference> {
        return collectionList.map { it.asCollectionReference() }
    }
    
    override fun getCollections(): MutableIterable<CollectionReference> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun document(path: String): DocumentReference {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getOptions(): FirestoreOptions {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun createCollection(collectionId: String) {
        collectionList += FakeCollectionReference(
            collectionId
        )
    }
}
