
// MFCWarrior.h : MFCWarrior ���� ���α׷��� ���� �� ��� ����
//
#pragma once

#ifndef __AFXWIN_H__
	#error "PCH�� ���� �� ������ �����ϱ� ���� 'stdafx.h'�� �����մϴ�."
#endif

#include "resource.h"       // �� ��ȣ�Դϴ�.


// CMFCWarriorApp:
// �� Ŭ������ ������ ���ؼ��� MFCWarrior.cpp�� �����Ͻʽÿ�.
//

class CMFCWarriorApp : public CWinApp
{
public:
	CMFCWarriorApp();


// �������Դϴ�.
public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

// �����Դϴ�.
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CMFCWarriorApp theApp;
