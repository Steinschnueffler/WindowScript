#pragma once

#ifdef EXPORT
#define WINDOW_SCRIPT_API __declspec(dllexport)
#else
#define WINDOW_SCRIPT_API __declspec(dllimport)
#endif

typedef int exit_code;

#define EXIT_CODE_NO_ERROR 0
#define EXIT_CODE_NO_ARG -1

namespace ws
{
	exit_code WINDOW_SCRIPT_API start(int argc, const char** argv, const char* exe);
}