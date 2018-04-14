// MySplitter.cpp : 구현 파일입니다.
//

#include "stdafx.h"
#include "MFCWarrior.h"
#include "MySplitter.h"


// CMySplitter

IMPLEMENT_DYNAMIC(CMySplitterWnd, CSplitterWnd)

CMySplitterWnd::CMySplitterWnd()
{

}

CMySplitterWnd::~CMySplitterWnd()
{
}



BEGIN_MESSAGE_MAP(CMySplitterWnd, CSplitterWnd)
END_MESSAGE_MAP()



// CMySplitter 메시지 처리기입니다.
void CMySplitterWnd::OnDrawSplitter(CDC* pDC, ESplitType nType, const CRect& rect)
{
	if (nType == splitBorder) {
		CClientDC dc(this);
		CPen *pBorderPen, *pOldPen;
		pBorderPen = new CPen(PS_SOLID, 3, RGB(68,68,68));
		pOldPen = dc.SelectObject(pBorderPen);
		dc.SelectStockObject(HOLLOW_BRUSH);
		dc.Rectangle(rect);
		dc.SelectObject(pOldPen);
		if (pBorderPen) delete pBorderPen;
	}
	if (nType == splitBar)
	{
		CSplitterWnd::OnDrawSplitter(pDC, nType, rect);
	}
}

