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

import java.util.Calendar;
import java.math.*;

public class CFLibArgumentRangeException extends CFLibArgumentException {

	public CFLibArgumentRangeException(
		String msg )
	{
		super( msg );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		String msg )
	{
		super( throwingClass, methName, msg );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, msg, th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String msg )
	{
		super( throwingClass, methName, argNo, argName, msg );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName, msg, th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short minValue,
		short maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " is out of the range " + Short.toString( minValue )
				+ ".."
				+ Short.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short minValue,
		short maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " is out of the range " + Short.toString( minValue )
				+ ".."
				+ Short.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int minValue,
		int maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " is out of the range " + Integer.toString( minValue )
				+ ".."
				+ Integer.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int minValue,
		int maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " is out of the range " + Integer.toString( minValue )
				+ ".."
				+ Integer.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long minValue,
		long maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " is out of the range " + Long.toString( minValue )
				+ ".."
				+ Long.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long minValue,
		long maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " is out of the range " + Long.toString( minValue )
				+ ".."
				+ Long.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float minValue,
		float maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " is out of the range " + Float.toString( minValue )
				+ ".."
				+ Float.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float minValue,
		float maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " is out of the range " + Float.toString( minValue )
				+ ".."
				+ Float.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double minValue,
		double maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " is out of the range " + Double.toString( minValue )
				+ ".."
				+ Double.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double minValue,
		double maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " is out of the range " + Double.toString( minValue )
				+ ".."
				+ Double.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar minValue,
		Calendar maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar minValue,
		Calendar maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String minValue,
		String maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String minValue,
		String maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal minValue,
		BigDecimal maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal minValue,
		BigDecimal maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
				"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String msg )
	{
		super( fieldName, msg );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		String msg )
	{
		super( fieldName, methName, msg );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		String msg,
		Throwable th )
	{
		super( fieldName, methName, msg, th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String msg )
	{
		super( fieldName, methName, argNo, argName, msg );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String msg,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName, msg, th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short minValue,
		short maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " is out of the range " + Short.toString( minValue )
				+ ".."
				+ Short.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short minValue,
		short maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " is out of the range " + Short.toString( minValue )
				+ ".."
				+ Short.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int minValue,
		int maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " is out of the range " + Integer.toString( minValue )
				+ ".."
				+ Integer.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int minValue,
		int maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " is out of the range " + Integer.toString( minValue )
				+ ".."
				+ Integer.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long minValue,
		long maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " is out of the range " + Long.toString( minValue )
				+ ".."
				+ Long.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long minValue,
		long maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " is out of the range " + Long.toString( minValue )
				+ ".."
				+ Long.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float minValue,
		float maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " is out of the range " + Float.toString( minValue )
				+ ".."
				+ Float.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float minValue,
		float maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " is out of the range " + Float.toString( minValue )
				+ ".."
				+ Float.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double minValue,
		double maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " is out of the range " + Double.toString( minValue )
				+ ".."
				+ Double.toString( maxValue ) );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double minValue,
		double maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " is out of the range " + Double.toString( minValue )
				+ ".."
				+ Double.toString( maxValue ),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar minValue,
		Calendar maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar minValue,
		Calendar maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String minValue,
		String maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String minValue,
		String maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal minValue,
		BigDecimal maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString() );
	}

	public CFLibArgumentRangeException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal minValue,
		BigDecimal maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
				"value " + argValue.toString()
				+ " is out of the range " + minValue.toString()
				+ ".."
				+ maxValue.toString(),
			th );
	}
}
