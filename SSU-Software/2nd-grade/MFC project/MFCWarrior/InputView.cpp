// InputView.cpp : 구현 파일입니다.
//

#include "stdafx.h"
#include "MFCWarrior.h"
#include "InputView.h"
#include "MFCWarriorDoc.h"
#include "OutputView.h"

// CInputView

IMPLEMENT_DYNCREATE(CInputView, CView)

CInputView::CInputView()
{

}

CInputView::~CInputView()
{
}

BEGIN_MESSAGE_MAP(CInputView, CView)
	ON_WM_ERASEBKGND()
	ON_WM_CHAR()
END_MESSAGE_MAP()


// CInputView 그리기입니다.

void CInputView::OnDraw(CDC* pDC)
{
	CMFCWarriorDoc * pDoc = (CMFCWarriorDoc *)GetDocument();

	CFont font;
	font.CreatePointFont(150, _T("바탕"));
	pDC->SelectObject(font);
	CRect rect;
	GetClientRect(&rect);
	pDC->SetBkMode(TRANSPARENT);
	pDC->SetTextColor(RGB(200, 200, 200));
	pDC->DrawText(pDoc->code_input.GetData(), pDoc->code_input.GetSize(), &rect, DT_LEFT);
	// TODO: 여기에 그리기 코드를 추가합니다.
}


// CInputView 진단입니다.

#ifdef _DEBUG
void CInputView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CInputView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG


// CInputView 메시지 처리기입니다.


BOOL CInputView::OnEraseBkgnd(CDC* pDC)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	CBrush backBrush(RGB(80, 80, 80));
	CBrush * pOldBrush = pDC->SelectObject(&backBrush);
	CRect rect;
	pDC->GetClipBox(&rect);
	pDC->PatBlt(rect.left, rect.top, rect.Width(), rect.Height(), PATCOPY);
	pDC->SelectObject(pOldBrush);
	return TRUE;

}


void CInputView::OnChar(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 여기에 메시지 처리기 코드를 추가 및/또는 기본값을 호출합니다.
	CMFCWarriorDoc * pDoc = (CMFCWarriorDoc *)GetDocument();
	pDoc->input_view = this;

	if (nChar == _T('\b'))
	{
		if (pDoc->code_input.GetSize() > 1) {
				pDoc->code_input.RemoveAt(pDoc->code_input.GetSize() - 2);
		}
	}
	else if (nChar == _T('&'))
	{
		if (pDoc->code_input.GetSize() > 0) {
			pDoc->code_input.RemoveAt(pDoc->code_input.GetSize() - 1);
			pDoc->code_input.Add('&');
			pDoc->code_input.Add('&');
			pDoc->code_input.Add('|');
		}
	}
	
	else if(nChar == VK_RETURN)
	{
		if (pDoc->stage > 0 && pDoc->stage <= 8){
			pDoc->output_view->OnCommand(100, 0);
			pDoc->output_view->first = true;
			pDoc->output_view->Invalidate();
		}
	}
	else {
		if (pDoc->code_input.GetSize() > 0) {
			pDoc->code_input.RemoveAt(pDoc->code_input.GetSize() - 1);
			pDoc->code_input.Add(nChar);
			pDoc->code_input.Add('|');
		}
		else {
			pDoc->code_input.Add(nChar);
			pDoc->code_input.Add('|');
		}
	}

	if (pDoc->stage == 1) {
		CString Stage_1_sample;
		Stage_1_sample = "AfxMessageBox(_T(\"앞으로가\"));";
		//Stage_1_sample = '1';
		CString Stage_1_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_1_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_1_answer.Remove(' ');
		Stage_1_answer.Remove('|');
		if (!Stage_1_sample.Compare(Stage_1_answer))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 2) {
		CString Stage_2_sample;
		Stage_2_sample = "dc.BitBlt(x, y, bi.bmWidth, bi.bmHeight, &&dcmem, 0, 0, bi.bmWidth, bi.bmHeight, SRCCOPY);";
		Stage_2_sample.Remove(' ');
		//Stage_2_sample = '1';

		CString Stage_2_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_2_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_2_answer.Remove(' ');
		Stage_2_answer.Remove('|');
		if (!Stage_2_sample.Compare(Stage_2_answer))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 3) {
		CString Stage_3_sample;
		Stage_3_sample = "dc.Rectangle(x-25,y-50,x+25,y+50);";
		//Stage_3_sample = '1';
		CString Stage_3_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_3_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_3_answer.Remove(' ');
		Stage_3_answer.Remove('|');
		if (!Stage_3_sample.Compare(Stage_3_answer))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 4)
	{
		CString Stage_4_sample;
		Stage_4_sample = "dc.LineTo(700,100);";
		//Stage_4_sample = '1';
		CString Stage_4_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_4_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_4_answer.Remove(' ');
		Stage_4_answer.Remove('|');
		if (!Stage_4_sample.Compare(Stage_4_answer))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 5) {
		CString Stage_5_sample;
		Stage_5_sample = "time=CTime::GetCurrentTime();";
		//Stage_5_sample = '1';
		CString Stage_5_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_5_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_5_answer.Remove(' ');
		Stage_5_answer.Remove('|');
		if (!Stage_5_sample.Compare(Stage_5_answer))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 6)
	{
		CString Stage_6_sample;
		Stage_6_sample = "password.InsertAt(3,2016);";
		//Stage_6_sample = '1';
		CString Stage_6_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_6_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_6_answer.Remove(' ');
		Stage_6_answer.Remove('|');
		if (!Stage_6_sample.Compare(Stage_6_answer))
		{
			pDoc->str_com = TRUE;
		}
		
	}
	else if (pDoc->stage == 7)
	{
		CString Stage_7_sample1, Stage_7_sample2;
		Stage_7_sample1 = "new.CombineRgn(&&original,&&circle,RGN_OR);";
		Stage_7_sample2 = "new.CombineRgn(&&circle,&&original,RGN_OR);";
		//Stage_7_sample1 = '1';

		CString Stage_7_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_7_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_7_answer.Remove(' ');
		Stage_7_answer.Remove('|');
		if ((!Stage_7_sample1.Compare(Stage_7_answer)) || (!Stage_7_sample2.Compare(Stage_7_answer)))
		{
			pDoc->str_com = TRUE;
		}
	}
	else if (pDoc->stage == 8)
	{
		CString Stage_8_sample;
		Stage_8_sample = "CBrushbrush(RGB(255,0,0));";
		//Stage_8_sample = '1';
		CString Stage_8_answer;
		for (int i = 0; i < pDoc->code_input.GetSize(); i++)
		{
			Stage_8_answer.Insert(i, pDoc->code_input[i]);
		}
		Stage_8_answer.Remove(' ');
		Stage_8_answer.Remove('|');
		if (!Stage_8_sample.Compare(Stage_8_answer))
		{
			pDoc->str_com = TRUE;
		}
	}

	

	Invalidate();
}
