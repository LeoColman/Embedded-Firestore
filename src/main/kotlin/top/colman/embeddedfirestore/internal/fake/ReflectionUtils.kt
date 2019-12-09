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

package top.colman.embeddedfirestore.internal.fake

import net.bytebuddy.description.ByteCodeElement
import net.bytebuddy.description.NamedElement
import net.bytebuddy.matcher.ElementMatcher.Junction
import net.bytebuddy.matcher.ElementMatchers.isDeclaredBy
import net.bytebuddy.matcher.ElementMatchers.named
import net.bytebuddy.matcher.ElementMatchers.not
import org.objenesis.ObjenesisStd

private val objenesis = ObjenesisStd()

internal fun <T> Class<T>.createInstance(): T = objenesis.newInstance(this)

internal inline fun <reified T> notFrom(): Junction<ByteCodeElement> = not(isDeclaredBy(T::class.java))

internal fun notNamed(name: String): Junction<NamedElement> = not(named(name))
