/*
 *	MSS Code Factory CFLib 2.13 Common Application Functionality
 *
 *	Copyright (C) 2016-2026 Mark Stephen Sobkow (mailto:mark.sobkow@gmail.com)
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

public class CFLibDependentsDetectedException extends CFLibRuntimeException {

	protected String relnType = null;
	protected String relnName = null;
	protected String relnTarget = null;
	protected Object pkey = null;

	public CFLibDependentsDetectedException(
		String msg )
	{
		super( msg );
	}
	
	public CFLibDependentsDetectedException(
		Class throwingClass,
		String methName,
		String msg )
	{
		super( throwingClass, methName, msg );
	}

	public CFLibDependentsDetectedException(
		Class throwingClass,
		String methName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, msg, th );
	}

	public CFLibDependentsDetectedException(
		Class throwingClass,
		String methName,
		String relationType,
		String relationName,
		String targetName,
		Object argKey )
	{
		super( throwingClass, methName,
			relationType + " relation " + relationName
				+ " dependents detected"
				+ (( argKey != null ) ? " for primary key " + argKey.toString() : "" ) );
		relnType = relationType;
		relnName = relationName;
		relnTarget = targetName;
		pkey = argKey;
	}

	public CFLibDependentsDetectedException(
		Class throwingClass,
		String methName,
		String relationType,
		String relationName,
		String targetName,
		Object argKey,
		Throwable th )
	{
		super( throwingClass, methName,
			relationType + " relation " + relationName
				+ " dependents detected"
				+ (( argKey != null ) ? " for primary key " + argKey.toString() : "" ),
			th );
		relnType = relationType;
		relnName = relationName;
		relnTarget = targetName;
		pkey = argKey;
	}

	public String getRelationType() {
		return( relnType );
	}
	
	public String getRelationName() {
		return( relnName );
	}
	
	public Object getPrimaryKey() {
		return( pkey );
	}
}
