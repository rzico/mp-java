@echo off
copy Mswinsck.OCX %windir%
regsvr32 %windir%\Mswinsck.OCX