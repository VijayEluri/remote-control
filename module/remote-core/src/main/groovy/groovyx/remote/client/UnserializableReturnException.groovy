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

import groovyx.remote.*

class UnserializableReturnException extends RemoteControlException {

	final String stringRepresentation
	
	UnserializableReturnException(Result result) {
		super("The return value of the command was not serializable, it's string representation was '$result.stringRepresentation'")
		this.stringRepresentation = result.stringRepresentation
	}

}