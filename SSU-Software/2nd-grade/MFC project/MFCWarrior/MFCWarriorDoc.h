
// MFCWarriorDoc.h : CMFCWarriorDoc Ŭ������ �������̽�
//


#pragma once
#include <afxtempl.h>
class CInputView;
class CCodeView;
class COutputView;

class CMFCWarriorDoc : public CDocument
{
protected: // serialization������ ��������ϴ�.
	CMFCWarriorDoc();
	DECLARE_DYNCREATE(CMFCWarriorDoc)

// Ư���Դϴ�.
public:
	CArray<TCHAR, TCHAR> code_input;
	BOOL str_com;
	CInputView * input_view;
	CCodeView * code_view;
	COutputView * output_view;

	int stage;
	int count;
	

// �۾��Դϴ�.
public:

// �������Դϴ�.
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
#ifdef SHARED_HANDLERS
	virtual void InitializeSearchContent();
	virtual void OnDrawThumbnail(CDC& dc, LPRECT lprcBounds);
#endif // SHARED_HANDLERS

// �����Դϴ�.
public:
	virtual ~CMFCWarriorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ������ �޽��� �� �Լ�
protected:
	DECLARE_MESSAGE_MAP()

#ifdef SHARED_HANDLERS
	// �˻� ó���⿡ ���� �˻� �������� �����ϴ� ����� �Լ�
	void SetSearchContent(const CString& value);
#endif // SHARED_HANDLERS
};
