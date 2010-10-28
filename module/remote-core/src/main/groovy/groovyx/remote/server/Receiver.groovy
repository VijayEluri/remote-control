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
package groovyx.remote.server

import groovyx.remote.*
import groovyx.remote.util.*

class Receiver {
	
	void execute(InputStream input, OutputStream output) {
		def commandChain = readCommandChain(input)
		def result = invokeCommandChain(commandChain)
		writeResult(result, output)
	}
	
	protected CommandChain readCommandChain(InputStream input) {
		new ClassLoaderConfigurableObjectInputStream(grailsApplication.classLoader, input).readObject()
	}
	
	protected Result invokeCommandChain(CommandChain commandChain) {
		new CommandChainInvoker(grailsApplication.classLoader, commandChain).invokeAgainst(createCommandDelegate())
	}
	
	protected createCommandDelegate() {
		new StorageCommandDelegate(createStorageCommandDelegateSeed())
	}
	
	protected Map createStorageCommandDelegateSeed() {
		[:]
	}
	
	protected writeResult(Result result, OutputStream output) {
		def oos = new ObjectOutputStream(output)
		oos << result
		oos.close()
	}
}

