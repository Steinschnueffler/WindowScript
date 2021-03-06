// WindowScript.cpp: Definiert die exportierten Funktionen für die DLL-Anwendung.
//

#include "stdafx.h"
#include "WindowScript.hpp"

#include <iostream>

namespace ws
{

	exit_code exit(int exitCode, const char* msg)
	{
		if (exitCode == EXIT_CODE_NO_ERROR)
			std::cout << msg << std::endl;
		else
			std::cerr << msg << std::endl;
		return exitCode;
	}

	exit_code WINDOW_SCRIPT_API start(int argc, const char** argv, const char* exe)
	{
		if (argc == 0)
			return exit(EXIT_CODE_NO_ARG, "Program must be started with a WindowScript!");
		return system(exe);
	}
}
