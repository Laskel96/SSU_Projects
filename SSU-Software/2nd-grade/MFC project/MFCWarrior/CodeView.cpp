// CodeView.cpp : 구현 파일입니다.
//

#include "stdafx.h"
#include "MFCWarrior.h"
#include "CodeView.h"
#include "OutputView.h"
#include "MFCWarriorDoc.h"


// CCodeView

IMPLEMENT_DYNCREATE(CCodeView, CView)

CCodeView::CCodeView()
{

}

CCodeView::~CCodeView()
{
}

BEGIN_MESSAGE_MAP(CCodeView, CView)
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()


// CCodeView 그리기입니다.

void CCodeView::OnDraw(CDC* pDC)
{
	CMFCWarriorDoc * pDoc = (CMFCWarriorDoc *)GetDocument();
	pDoc->code_view = this;
	CRect rect;
	GetClientRect(&rect);
	pDC->SetTextColor(RGB(255, 255, 255));
	pDC->SetBkMode(TRANSPARENT);
	CFont font;
	font.CreatePointFont(150, _T("바탕"));
	pDC->SelectObject(font);

	if (pDoc->stage == 1)
	{
		CString s1;
		s1 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                    dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                        this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                             );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n\n      (                      )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);


	}
	else if (pDoc->stage == 2)
	{
		//CString Stage_2_output;
		//Stage_2_output = "void CChildView::OnPaint()\n{\n        CPaintDC dc(this);\n        CBitmap bitmap;\n        BITMAP bitinfo;\n        CDC dcmem;\n        bitmap.LoadBitmap(IDB_SWORD);\n        bitmap.GetBitmap(&&bitinfo);\n        dcmem.CreateCompatibleDC(&&dc);\n        dcmem.SelectObject(&&bitmap);\n        (                                                                            )\n}";
		//pDC->DrawText(Stage_2_output, rect, DT_LEFT);
		CString s2;
		s2 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "                       ::OnPaint(){\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n                    dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n                        this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n                             );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n      CBitmap";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n                   m1;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n      BITMAP";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n                   bi;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n      CDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n             dcmem;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n      m1.LoadBitmap(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n                            IDB_SWORD";
		pDC->SetTextColor(RGB(189, 99, 197));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n                                             );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n\n      m1.GetBitmap(&&bi);";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n\n\n      dcmem.CreateCompatibleDC(&&dc);";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n\n\n\n      dcmem.SelectObject(&&m1);";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);
		s2 = "\n\n\n\n\n\n\n\n\n      (                              )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s2, rect, DT_LEFT);



	}
	else if (pDoc->stage == 3)
	{
		CString s1;
		s1 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                    dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                        this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n                             );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
		s1 = "\n\n\n      (                      )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s1, rect, DT_LEFT);
	}
	else if (pDoc->stage == 4)
	{
		CString s4;
		s4 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n                     dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n                         this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n                              );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n\n      dc.";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n\n          MoveTo";
		pDC->SetTextColor(RGB(189, 99, 197));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n\n                     (400 , 100);";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
		s4 = "\n\n\n\n      (                            )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s4, rect, DT_LEFT);
	}
	else if (pDoc->stage == 5)
	{
		CString s5;
		s5 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n      CTime";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n                time;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n      CString";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n                 TimeData;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n      (                       )";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n\n      TimeData.Format(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n\n                               _T";
		pDC->SetTextColor(RGB(189, 99, 197));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n\n                                   (";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n\n                                    \" 현재 시각은 %d시 %d분 %d초입니다 \"";
		pDC->SetTextColor(RGB(192, 157, 133));
		pDC->DrawText(s5, rect, DT_LEFT);
		s5 = "\n\n\n\n\n                                                                                            ) , \n      time.GetHour() , time.GetMinute() , time.GetSecond());\n}";
		pDC->SetTextColor(RGB(255, 1255, 255));
		pDC->DrawText(s5, rect, DT_LEFT);

	}
	else if (pDoc->stage == 6)
	{
		CString s6;
		s6 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n      CArray";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n               < ";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n                 int";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n                    >  ";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n                       password;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s6, rect, DT_LEFT);
		s6 = "\n\n\n      password.SetSize(5);\n      (                     )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s6, rect, DT_LEFT);



	}
	else if (pDoc->stage == 7)
	{
		CString s7;
		s7 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n                     dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n                         this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n                              );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n\n      CRgn";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n\n              original, new, circle;";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n\n\n      new.CreateCircle(0,0,1,1);\n      original.CreateCircle(x, y, x + a, y + a);\n      circle.CreateCircle(x, y, x + a, y + a);";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);
		s7 = "\n\n\n\n\n\n\n      (                      )\n}";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s7, rect, DT_LEFT);

	}
	else if (pDoc->stage == 8)
	{
		CString s8;
		s8 = "void";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "       CChildView";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "                       ::OnPaint()\n{\n";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n      CPaintDC";
		pDC->SetTextColor(RGB(78, 201, 176));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n                     dc(";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n                         this";
		pDC->SetTextColor(RGB(50, 156, 214));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n                              );";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n\n      (                      )";
		pDC->SetTextColor(RGB(255, 255, 255));
		pDC->DrawText(s8, rect, DT_LEFT);
		s8 = "\n\n\n\n      dc.SelectObject(&&brush);\n      dc.Ellipse(80, 100, 180, 200);\n}";
		pDC->DrawText(s8, rect, DT_LEFT);

	}
}


// CCodeView 진단입니다.

#ifdef _DEBUG
void CCodeView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CCodeView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG


// CCodeView 메시지 처리기입니다.


BOOL CCodeView::OnEraseBkgnd(CDC* pDC)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	CBrush backBrush(RGB(68, 68, 68));
	CBrush * pOldBrush = pDC->SelectObject(&backBrush);
	CRect rect;
	pDC->GetClipBox(&rect);
	pDC->PatBlt(rect.left, rect.top, rect.Width(), rect.Height(), PATCOPY);
	pDC->SelectObject(pOldBrush);
	return TRUE;
}
