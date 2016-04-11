echo off
cls
rem faire attention au point virgule car sur windows
java -classpath sq.jar;bin ch.epfl.xblast.server.debug.RandomGame
pause