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

/**
 * Used for abitrary name/value storage, but throws MPE on get of non existant property.
 */
class StorageCommandDelegate {

	private final storage
	
	StorageCommandDelegate(Map storage) {
		this.storage = storage
	}
	
	def propertyMissing(String name) {
		if (storage.containsKey(name)) {
			storage[name]
		} else {
			throw new MissingPropertyException("No property named '$name' is associated with the command delegate".toString())
		}
	}

	void propertyMissing(String name, value) {
		storage[name] = value
	}
}

