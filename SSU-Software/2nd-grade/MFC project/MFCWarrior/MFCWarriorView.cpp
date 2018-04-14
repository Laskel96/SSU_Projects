
// MFCWarriorView.cpp : CMFCWarriorView Ŭ������ ����
//

#include "stdafx.h"
// SHARED_HANDLERS�� �̸� ����, ����� �׸� �� �˻� ���� ó���⸦ �����ϴ� ATL ������Ʈ���� ������ �� ������
// �ش� ������Ʈ�� ���� �ڵ带 �����ϵ��� �� �ݴϴ�.
#ifndef SHARED_HANDLERS
#include "MFCWarrior.h"
#endif

#include "MFCWarriorDoc.h"
#include "MFCWarriorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMFCWarriorView

IMPLEMENT_DYNCREATE(CMFCWarriorView, CView)

BEGIN_MESSAGE_MAP(CMFCWarriorView, CView)
	// ǥ�� �μ� ����Դϴ�.
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CView::OnFilePrintPreview)
//	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

// CMFCWarriorView ����/�Ҹ�

CMFCWarriorView::CMFCWarriorView()
{
	// TODO: ���⿡ ���� �ڵ带 �߰��մϴ�.

}

CMFCWarriorView::~CMFCWarriorView()
{
}

BOOL CMFCWarriorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: CREATESTRUCT cs�� �����Ͽ� ���⿡��
	//  Window Ŭ���� �Ǵ� ��Ÿ���� �����մϴ�.

	return CView::PreCreateWindow(cs);
}

// CMFCWarriorView �׸���

void CMFCWarriorView::OnDraw(CDC* /*pDC*/)
{
	CMFCWarriorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: ���⿡ ���� �����Ϳ� ���� �׸��� �ڵ带 �߰��մϴ�.
}


// CMFCWarriorView �μ�

BOOL CMFCWarriorView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// �⺻���� �غ�
	return DoPreparePrinting(pInfo);
}

void CMFCWarriorView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: �μ��ϱ� ���� �߰� �ʱ�ȭ �۾��� �߰��մϴ�.
}

void CMFCWarriorView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: �μ� �� ���� �۾��� �߰��մϴ�.
}


// CMFCWarriorView ����

#ifdef _DEBUG
void CMFCWarriorView::AssertValid() const
{
	CView::AssertValid();
}

void CMFCWarriorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CMFCWarriorDoc* CMFCWarriorView::GetDocument() const // ����׵��� ���� ������ �ζ������� �����˴ϴ�.
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CMFCWarriorDoc)));
	return (CMFCWarriorDoc*)m_pDocument;
}
#endif //_DEBUG


// CMFCWarriorView �޽��� ó����


//BOOL CMFCWarriorView::OnEraseBkgnd(CDC* pDC)
//{
//	// TODO: ���⿡ �޽��� ó���� �ڵ带 �߰� ��/�Ǵ� �⺻���� ȣ���մϴ�.
//	CBrush backBrush(RGB(0, 0, 255));
//	CBrush * pOldBrush = pDC->SelectObject(&backBrush);
//	CRect rect;
//	pDC->GetClipBox(&rect);
//	pDC->PatBlt(rect.left, rect.top, rect.Width(), rect.Height(), PATCOPY);
//	pDC->SelectObject(pOldBrush);
//	return TRUE;
//}
