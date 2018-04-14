#pragma once


// CMySplitter

class CMySplitterWnd : public CSplitterWnd
{
	DECLARE_DYNAMIC(CMySplitterWnd)

protected:
	virtual void OnDrawSplitter(CDC* pDC, ESplitType nType, const CRect& rect);
public:
	CMySplitterWnd();
	virtual ~CMySplitterWnd();

protected:
	DECLARE_MESSAGE_MAP()
};


