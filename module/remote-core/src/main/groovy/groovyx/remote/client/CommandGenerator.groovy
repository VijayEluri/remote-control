/*
 * Copyright 2010 Luke Daley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.remote.client

import groovyx.remote.Command
import groovyx.remote.util.ClosureUtils

/**
 * Serialises closures to Command objects that are suitable for transmission.
 * 
 * Finds the corresponding .class file for the closure and any inner closures.
 */
class CommandGenerator {

	final ClassLoader classLoader

	CommandGenerator() {
		this(null)
	}
	
	CommandGenerator(ClassLoader classLoader) {
		this.classLoader = classLoader ?: Thread.currentThread().contextClassLoader
	}
	
	Command generate(Closure closure) {
		def closureClass = ClosureUtils.getRootClosure(closure).class
		
		new Command(
			instance: serializeInstance(closure),
			root: getClassFile(closureClass).bytes,
			supports: new InnerClosureClassDefinitionsFinder(classLoader).find(closureClass)
		)
	}
	
	protected File getClassFile(Class closureClass) {
		def classFileResource = classLoader.findResource(getClassFileResourceName(closureClass))
		if (classFileResource == null) {
			throw new IllegalStateException("Could not find class file for class ${closureClass}")
		}
		
		new File(classFileResource.file)
	}
	
	protected String getClassFileResourceBase(Class closureClass) {
		closureClass.name.replace('.', '/')
	}
	
	protected String getClassFileResourceName(Class closureClass) {
		getClassFileResourceBase(closureClass) + ".class"
	}
	
	protected serializeInstance(Closure closure) {
		def cloned = closure.clone()
		
		def nullOutTarget = ClosureUtils.getRootClosure(cloned)
		Closure.metaClass.setAttribute(nullOutTarget, 'owner', null)
		Closure.metaClass.setAttribute(nullOutTarget, 'thisObject', null)
		nullOutTarget.delegate = null
		
		def baos = new ByteArrayOutputStream()
		def oos = new ObjectOutputStream(baos)
		oos.writeObject(cloned)
		oos.close()
		
		baos.toByteArray()
	}
	
}