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

package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import java.util.*;

public class CFLibDbUtil {

	public final static TimeZone tzUTC = TimeZone.getTimeZone( "GMT+0000" );
	public final static Calendar localCalendar = new GregorianCalendar();
	public final static int localTZOffsetMillis = localCalendar.get( Calendar.ZONE_OFFSET );
	public static int dbServerTZOffsetMillis = localTZOffsetMillis;
	public static TimeZone dbServerTimeZone = null;

	public static int getDbServerTZOffsetMillis() {
		return( dbServerTZOffsetMillis );
	}
	
	public static void setDbServerTZOffsetMillis( int value ) {
		dbServerTZOffsetMillis = value;
		dbServerTimeZone = null;
	}

	public static TimeZone getDbServerTimeZone() {
		if( dbServerTimeZone == null ) {
			int secondsOnly = dbServerTZOffsetMillis / 1000;
			int minutesOnly = secondsOnly / 60;
			int absMinutes = ( minutesOnly < 0 ) ? ( 0 - minutesOnly ) : minutesOnly;
			int minutes = absMinutes % 60;
			int hours = absMinutes / 60;
			StringBuffer buff = new StringBuffer();
			Formatter fmt = new Formatter( buff );
			if( minutesOnly < 0 ) {
				buff.append( "GMT-" );
			}
			else {
				buff.append( "GMT+" );
			}
			fmt.format( "%1$02d", hours );
			fmt.format( "%1$02d", minutes );
			dbServerTimeZone = TimeZone.getTimeZone( buff.toString() );
			fmt.close();
		}
		return( dbServerTimeZone );
	}
	
	public static void setDbServerTimeZone( TimeZone tz ) {
		if( tz == null ) {
			throw new IllegalArgumentException( "TimeZone tz is null" );
		}
		dbServerTimeZone = tz;
		dbServerTZOffsetMillis = tz.getRawOffset();
	}

	public static Calendar getDbServerCalendar( Calendar value ) {
		if( value == null ) {
			return( null );
		}
		Calendar cal = Calendar.getInstance( getDbServerTimeZone() );
		cal.setTimeInMillis( value.getTimeInMillis() );
		return( cal );
	}

	public static Calendar getUTCCalendar( Calendar value ) {
		if( value == null ) {
			return( null );
		}
		Calendar cal = Calendar.getInstance( tzUTC );
		cal.setTimeInMillis( value.getTimeInMillis() );
		return( cal );
	}
}
