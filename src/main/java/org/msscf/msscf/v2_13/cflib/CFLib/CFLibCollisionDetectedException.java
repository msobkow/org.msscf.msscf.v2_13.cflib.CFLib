/*
 *	MSS Code Factory CFLib 2.13 Common Application Functionality
 *
 *	Copyright (C) 2016-2025 Mark Stephen Sobkow (mailto:mark.sobkow@gmail.com)
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see &lt;https://www.gnu.org/licenses/&gt;.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 */

package org.msscf.msscf.v2_13.cflib.CFLib;

public class CFLibCollisionDetectedException extends CFLibRuntimeException {

	protected Object indexKey = null;

	public CFLibCollisionDetectedException(
		String msg )
	{
		super( msg );
	}

	public CFLibCollisionDetectedException(
		Class throwingClass,
		String methName,
		String msg )
	{
		super( throwingClass, methName, msg );
	}

	public CFLibCollisionDetectedException(
		Class throwingClass,
		String methName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, msg, th );
	}

	public CFLibCollisionDetectedException(
		Class throwingClass,
		String methName,
		Object argKey )
	{
		super( throwingClass, methName,
			"Collision detected"
				+ ( ( argKey != null )
						? " for primary key " + argKey.toString()
						: "" ) );
		indexKey = argKey;
	}

	public CFLibCollisionDetectedException(
		Class throwingClass,
		String methName,
		Object argKey,
		Throwable th )
	{
		super( throwingClass, methName,
			"Collision detected"
				+ ( ( argKey != null )
						? " for primary key " + argKey.toString()
						: "" ),
			th );
		indexKey = argKey;
	}

	public Object getIndexKey() {
		return( indexKey );
	}
}
