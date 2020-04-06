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

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.QueryDocumentSnapshot
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.isPublic
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.from
import top.colman.embeddedfirestore.internal.fake.notNamed

internal class FakeQueryDocumentSnapshot(
    private val fakeDocumentSnapshot: FakeDocumentSnapshot
) {
    fun getData(): Map<String, Any> = fakeDocumentSnapshot.getData()
    
    fun asQueryDocumentSnapshot(): QueryDocumentSnapshot {
        return ByteBuddy()
            .subclass(QueryDocumentSnapshot::class.java)
            .method(from<QueryDocumentSnapshot>().and<MethodDescription>(isPublic()).and(notNamed("toObject")))
            .intercept(MethodDelegation.to(this))
            .method(from<DocumentSnapshot>().and<MethodDescription>(isPublic()).and(notNamed("toObject")))
            .intercept(MethodDelegation.to(fakeDocumentSnapshot))
            .make()
            .load(this::class.java.classLoader)
            .loaded.createInstance()
    }
}

