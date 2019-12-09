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

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.notFrom

class FakeDocumentReference {

    fun getFirestore(): Firestore = TODO()
    
    fun getId(): String = TODO()
    
    fun getPath(): String = TODO()
    
    
    fun asDocumentReference(): DocumentReference {
        return ByteBuddy()
            .subclass(DocumentReference::class.java)
            .method(notFrom<Any>())
            .intercept(MethodDelegation.to(this))
            .make()
            .load(DocumentReference::class.java.classLoader)
            .loaded.createInstance()
    }
}