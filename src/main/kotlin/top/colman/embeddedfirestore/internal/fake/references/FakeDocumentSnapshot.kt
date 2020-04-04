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

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Blob
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.GeoPoint
import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.isPublic
import top.colman.embeddedfirestore.internal.fake.createInstance
import top.colman.embeddedfirestore.internal.fake.notNamed
import java.util.Date
import java.util.Objects

@Suppress("TooManyFunctions")
internal class FakeDocumentSnapshot(
    private val reference: FakeDocumentReference
) {
    
    private val fields get() = reference.fields
    
    fun getId(): String = reference.id
    
    fun getReadTime(): Timestamp = TODO()
    
    fun getUpdateTime(): Timestamp = TODO()
    
    fun getCreateTime(): Timestamp = TODO()
    
    fun exists(): Boolean = TODO()
    
    fun getData(): Map<String, Any> = fields
    
    fun contains(field: String): Boolean = fields.containsKey(field)
    
    fun contains(fieldPath: FieldPath): Boolean = TODO()
    
    fun get(field: String): Any? = reference.fields[field]
    
    fun get(fieldPath: FieldPath): Any? = TODO()
    
    fun getBoolean(field: String): Boolean? = get(field) as Boolean?
    
    fun getDouble(field: String): Double? = get(field) as Double?
    
    fun getString(field: String): String? = get(field) as String?
    
    fun getLong(field: String): Long? = get(field) as Long?
    
    fun getDate(field: String): Date? = get(field) as Date?
    
    fun getTimestamp(field: String): Timestamp? = get(field) as Timestamp?
    
    fun getBlob(field: String): Blob? = get(field) as Blob?
    
    fun getGeoPoint(field: String): GeoPoint? = get(field) as GeoPoint?
    
    fun getReference(): DocumentReference = reference.asDocumentReference()

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other == null || other !is FakeDocumentSnapshot) return false
        return reference == other.reference && fields == other.fields 
    }

    override fun hashCode(): Int = Objects.hash(reference, fields)

    fun asDocumentSnapshot(): DocumentSnapshot {
        return ByteBuddy()
            .subclass(DocumentSnapshot::class.java)
            .method(
                notNamed("getResourcePath")
                    .and(notNamed("clone"))
                    .and(notNamed("toObject"))
                    .and(isPublic())
            )
            .intercept(MethodDelegation.to(this))
            .make()
            .load(DocumentReference::class.java.classLoader)
            .loaded.createInstance()
    }
}
