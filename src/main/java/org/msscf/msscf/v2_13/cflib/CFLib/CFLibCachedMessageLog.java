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

import java.io.*;

public class CFLibCachedMessageLog
implements ICFLibMessageLog {

	private StringBuffer cacheContents = new StringBuffer();
	private int		   	indent = 0;
	
	/**
	 *	Default constructor.
	 */
	public CFLibCachedMessageLog() {
		indent = 0;
	}

	public void clearCache() {
		cacheContents.setLength( 0 );
	}

	public String getCacheContents() {
		return( cacheContents.toString() );
	}
	
	public int getMessageLogIndent() {
		return( indent );
	}
	
	public synchronized void dedent() {
		if( indent > 0 ) {
			indent --;
		}
		else {
			indent = 0;
		}
	}
	
	public synchronized void indent() {
		indent ++;
	}
	
	public synchronized void message( String msg ) {

		if( msg == null ) {
			return;
		}

		int			i;
		for( i = ( ( cacheContents.length() <= 0 ) ? 1 : 0 ); i < indent; i ++ ) {
			cacheContents.append( "\t" );
		}
		cacheContents.append( msg );
		if( ! msg.endsWith( "\n" ) ) {
			cacheContents.append( "\n" );
		}
	}

//	OLD API's that need to go away

	public PrintStream getPrintStream() {
		return( null );
	}

	public void openLogFile( String fileName )
		throws FileNotFoundException
	{
		// Do-nothing stub; cached message logs don't write to files
	}
	
	public void closeLogFile() {
		// Do-nothing stub; cached message logs don't write to files
	}
}
