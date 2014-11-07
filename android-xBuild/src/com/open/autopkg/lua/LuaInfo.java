package com.open.autopkg.lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import com.open.autopkg.data.Config.LuaZipBean;

public class LuaInfo {
	
	public static final String G_HALL_PKGNAME = "hall";
	
	public String hallVersion;
	public String hallDepApkVersion;
	public List<Map<String, String>> gameVersionList = new ArrayList<Map<String,String>>();
	
	private String errInfo = "success";
	
	public String initGameVersion(String luaPrjPath,ArrayList<LuaZipBean> list)
	{
		for(int i=0; i<list.size(); ++i)
		{
			initGame(luaPrjPath, list.get(i).gameEntry, list.get(i).gamePkg);
		}
		return errInfo;
	}
	
	private String initGame(String luaPrjPath, String luafile, String gamePkg){
		LuaState L = LuaStateFactory.newLuaState();
	    L.openLibs();
	    int err = L.LdoFile(luaPrjPath+"\\Resource\\scripts" + luafile);
	    if(err != 0)
	    {
		      switch (err)
		      {
			        case 1 :
			          System.out.println("Runtime error. " + L.toString(-1));
			          errInfo = "Runtime error. " + L.toString(-1);
			          break;
		
			        case 2 :
			          System.out.println("File not found. " + L.toString(-1));
			          errInfo = "File not found. " + L.toString(-1);
			          break;
		
			        case 3 :
			          System.out.println("Syntax error1. " + L.toString(-1));
			          errInfo = "Syntax error. " + L.toString(-1);
			          break;
			        
			        case 4 :
			          System.out.println("Memory error. " + L.toString(-1));
			          errInfo = "Memory error. " + L.toString(-1);
			          break;
			        
			        default :
			          System.out.println("Error. " + L.toString(-1));
			          errInfo = "Error. " + L.toString(-1);
			          break;
		      }
	    }
	    
	    String gameName 	= L.getLuaObject("_gameName").getString();
	    String gameversion = L.getLuaObject("_gameVersion").getString();
	    
	    if(G_HALL_PKGNAME.equals(gamePkg))
	    {
	    	hallDepApkVersion = L.getLuaObject("_dependApkVersion").getString();
		    hallVersion = gameversion;
		    return "success";
	    }
	    
	    HashMap<String, String> map = new HashMap<>();
	    map.put("gamename", gameName);
	    map.put("gameversion", gameversion);
	    gameVersionList.add(map);
	    
		return errInfo;
	}
	
	

}
