Attribute VB_Name = "UTF8"
Private Declare Function WideCharToMultiByte Lib "kernel32" (ByVal CodePage As Long, ByVal dwFlags As Long, ByVal lpWideCharStr As Long, ByVal cchWideChar As Long, ByRef lpMultiByteStr As Any, ByVal cchMultiByte As Long, ByVal lpDefaultChar As String, ByVal lpUsedDefaultChar As Long) As Long
Private Declare Function MultiByteToWideChar Lib "kernel32" (ByVal CodePage As Long, ByVal dwFlags As Long, ByVal lpMultiByteStr As Long, ByVal cchMultiByte As Long, ByVal lpWideCharStr As Long, ByVal cchWideChar As Long) As Long
Private Const CP_UTF8 = 65001
Private Const CP_ACP As Long = 0

'�ӿ����ͣ��������߹��ʶ��Žӿڡ�
'�˻�ע�᣺��ͨ���õ�ַ��ͨ�˻�http://sms.ihuyi.com/register.html
'ע�����
'��1�������ڼ䣬����ϸ�Ķ��ӿ��ĵ���
'��2����ʹ��APIID���鿴APIID���¼�û�����->���ʶ���->��Ʒ����->APIID����APIkey�����ýӿڣ�
'��3���ô���������뻥�����߶��Žӿڲο�ʹ�ã��ͻ��ɸ���ʵ����Ҫ���б�д��

Public Function toUTF8(szInput)
     Dim wch, uch, szRet
     Dim x
     Dim nAsc, nAsc2, nAsc3
     '����������Ϊ�գ����˳�����
     If szInput = "" Then
         toUTF8 = szInput
         Exit Function
     End If
     '��ʼת��
      For x = 1 To Len(szInput)
         '����mid�����ֲ�GB��������
         wch = Mid(szInput, x, 1)
         '����ascW��������ÿһ��GB�������ֵ�Unicode�ַ�����
         'ע��asc�������ص���ANSI �ַ����룬ע������
         nAsc = AscW(wch)
         If nAsc < 0 Then nAsc = nAsc + 65536
    
         If (nAsc And &HFF80) = 0 Then
             szRet = szRet & wch
         Else
             If (nAsc And &HF000) = 0 Then
                 uch = "%" & Hex(((nAsc \ 2 ^ 6)) Or &HC0) & Hex(nAsc And &H3F Or &H80)
                 szRet = szRet & uch
             Else
                'GB�������ֵ�Unicode�ַ�������0800 - FFFF֮��������ֽ�ģ��
                 uch = "%" & Hex((nAsc \ 2 ^ 12) Or &HE0) & "%" & _
                             Hex((nAsc \ 2 ^ 6) And &H3F Or &H80) & "%" & _
                             Hex(nAsc And &H3F Or &H80)
                 szRet = szRet & uch
             End If
         End If
     Next
     toUTF8 = szRet
     
     toUTF8 = Replace(toUTF8, Chr(13) + Chr(10), "%0D%0A")
     toUTF8 = Replace(toUTF8, " ", "%20")
     toUTF8 = Replace(toUTF8, "+", "%2B")

End Function



Public Function Utf8ToUnicode(ByRef Utf() As Byte) As String
    Dim lRet As Long
    Dim lLength As Long
    Dim lBufferSize As Long
    lLength = UBound(Utf) - LBound(Utf) + 1
    If lLength <= 0 Then Exit Function
    lBufferSize = lLength * 2
    Utf8ToUnicode = String$(lBufferSize, Chr(0))
    lRet = MultiByteToWideChar(CP_UTF8, 0, VarPtr(Utf(0)), lLength, StrPtr(Utf8ToUnicode), lBufferSize)
    If lRet <> 0 Then
    Utf8ToUnicode = Left(Utf8ToUnicode, lRet)
    End If
End Function

Public Function URLEncode(vstrIn)
   strReturn = ""
   Dim i
   For i = 1 To Len(vstrIn)
   ThisChr = Mid(vstrIn, i, 1)
   If Abs(Asc(ThisChr)) < &HFF Then
   strReturn = strReturn & ThisChr
   Else
   innerCode = Asc(ThisChr)
  If innerCode < 0 Then
  innerCode = innerCode + &H10000
  End If
  Hight8 = (innerCode And &HFF00) \ &HFF
  Low8 = innerCode And &HFF
  strReturn = strReturn & "%" & Hex(Hight8) & "%" & Hex(Low8)
  End If
  Next
  strReturn = Replace(strReturn, Chr(32), "%20")
  strReturn = Replace(strReturn, "+", "%2B")
  strReturn = Replace(strReturn, " ", "+")
  strReturn = Replace(strReturn, vbCrLf, "%0D%0A")
  strReturn = Replace(strReturn, "#", "%23")
  URLEncode = strReturn
End Function
