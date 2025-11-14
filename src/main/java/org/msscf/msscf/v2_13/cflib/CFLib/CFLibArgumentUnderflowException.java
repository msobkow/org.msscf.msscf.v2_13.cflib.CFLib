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

public class CFLibArgumentUnderflowException extends CFLibArgumentException {

	public CFLibArgumentUnderflowException(
		String msg )
	{
		super( msg );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		String msg )
	{
		super( throwingClass, methName, msg );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, msg, th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String msg )
	{
		super( throwingClass, methName, argNo, argName, msg );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String msg,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName, msg, th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " must be at least "
				+ Short.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " must be at least "
				+ Short.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " must be at least "
				+ Integer.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " must be at least "
				+ Integer.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " must be at least "
				+ Long.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " must be at least "
				+ Long.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " must be at least "
				+ Float.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " must be at least "
				+ Float.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " must be at least "
				+ Double.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " must be at least "
				+ Double.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal maxValue )
	{
		super( throwingClass, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		Class throwingClass,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal maxValue,
		Throwable th )
	{
		super( throwingClass, methName, argNo, argName,
				"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String msg )
	{
		super( fieldName, msg );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		String msg )
	{
		super( fieldName, methName, msg );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		String msg,
		Throwable th )
	{
		super( fieldName, methName, msg, th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String msg )
	{
		super( fieldName, methName, argNo, argName, msg );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String msg,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName, msg, th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " must be at least "
				+ Short.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		short argValue,
		short maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Short.toString( argValue )
				+ " must be at least "
				+ Short.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " must be at least "
				+ Integer.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		int argValue,
		int maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Integer.toString( argValue )
				+ " must be at least "
				+ Integer.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " must be at least "
				+ Long.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		long argValue,
		long maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Long.toString( argValue )
				+ " must be at least "
				+ Long.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " must be at least "
				+ Float.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		float argValue,
		float maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Float.toString( argValue )
				+ " must be at least "
				+ Float.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " must be at least "
				+ Double.toString( maxValue ) );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		double argValue,
		double maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + Double.toString( argValue )
				+ " must be at least "
				+ Double.toString( maxValue ),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		Calendar argValue,
		Calendar maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		String argValue,
		String maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal maxValue )
	{
		super( fieldName, methName, argNo, argName,
			"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString() );
	}

	public CFLibArgumentUnderflowException(
		String fieldName,
		String methName,
		int argNo,
		String argName,
		BigDecimal argValue,
		BigDecimal maxValue,
		Throwable th )
	{
		super( fieldName, methName, argNo, argName,
				"value " + argValue.toString()
				+ " must be at least "
				+ maxValue.toString(),
			th );
	}
}
