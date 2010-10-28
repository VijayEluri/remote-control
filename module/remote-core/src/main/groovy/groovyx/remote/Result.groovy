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
package groovyx.remote

class Result implements Serializable {

	boolean wasNull = false
	boolean wasUnserializable = false
	boolean wasThrown = false
	String stringRepresentation = null
	Serializable value = null
	
	transient unserializable = null
	
	static forNull() {
		new Result(wasNull: true)
	}
	
	static forValue(value) {
		if (value == null) {
			forNull()
		} else if (value instanceof Serializable) {
			forSerializable(value)
		} else {
			forUnserializable(value)
		}
	}
	
	static forThrown(Throwable thrown) {
		new Result(
			wasThrown: true,
			value: thrown
		)
	}
	
	private static forUnserializable(unserializable) {
		new Result(
			wasUnserializable: true,
			stringRepresentation: unserializable.toString(),
			unserializable: unserializable
		)
	}
	
	private static forSerializable(Serializable serializable) {
		new Result(
			value: serializable,
			stringRepresentation: serializable.toString()
		)
	}
}

