
// MFCWarriorView.h : CMFCWarriorView Ŭ������ �������̽�
//

#pragma once


class CMFCWarriorView : public CView
{
protected: // serialization������ ��������ϴ�.
	CMFCWarriorView();
	DECLARE_DYNCREATE(CMFCWarriorView)

// Ư���Դϴ�.
public:
	CMFCWarriorDoc* GetDocument() const;

// �۾��Դϴ�.
public:

// �������Դϴ�.
public:
	virtual void OnDraw(CDC* pDC);  // �� �並 �׸��� ���� �����ǵǾ����ϴ�.
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

// �����Դϴ�.
public:
	virtual ~CMFCWarriorView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ������ �޽��� �� �Լ�
protected:
	DECLARE_MESSAGE_MAP()
public:
//	afx_msg BOOL OnEraseBkgnd(CDC* pDC);
};

#ifndef _DEBUG  // MFCWarriorView.cpp�� ����� ����
inline CMFCWarriorDoc* CMFCWarriorView::GetDocument() const
   { return reinterpret_cast<CMFCWarriorDoc*>(m_pDocument); }
#endif

