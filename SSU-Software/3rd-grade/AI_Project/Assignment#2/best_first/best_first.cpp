// best_first.cpp : 콘솔 응용 프로그램에 대한 진입점을 정의합니다.
//

#include "stdafx.h"
#include<iostream>
#define ElementSize 20

using namespace std;

typedef struct node {
	int num_parent;
	int num;
	int cost;
	int f;
}node;

void swap(int i, int j, node *a) {
	node temp = a[i];
	a[i] = a[j];
	a[j] = temp;
}

void quicksort(node *arr, int left, int right) {
	int min = (left + right) / 2;
	int i = left;
	int j = right;
	int pivot = arr[min].f;

	while (left<j || i<right)
	{
		while (arr[i].f<pivot)
			i++;
		while (arr[j].f>pivot)
			j--;

		if (i <= j) {
			swap(i, j, arr);
			i++;
			j--;
		}
		else {
			if (left<j)
				quicksort(arr, left, j);
			if (i<right)
				quicksort(arr, i, right);
			return;
		}
	}
}
char * city_name(int i)
{
	if (i == 0) return "Arad";
	else if (i == 1) return "Bucharest";
	else if (i == 2) return "Craiova";
	else if (i == 3) return "Dobreta";
	else if (i == 4) return "Eforie";
	else if (i == 5) return "Giurgiu";
	else if (i == 6) return "Hirsova";
	else if (i == 7) return "Iasi";
	else if (i == 8) return "Lugoj";
	else if (i == 9) return "Mehadia";
	else if (i == 10) return "Neamt";
	else if (i == 11) return "Oradea";
	else if (i == 12) return "Pitesti";
	else if (i == 13) return "Rimnicu vilcea";
	else if (i == 14) return "Sibiu";
	else if (i == 15) return "Timisoara";
	else if (i == 16) return "Urziceni";
	else if (i == 17) return "Vaslui";
	else if (i == 18) return "Yagaras";
	else return "Zerind";
}

int main()
{
	int W[ElementSize][ElementSize];//replacement of graph
	int H[ElementSize];
	node X;
	int start = 15;
	int goal = 1;
	node open[ElementSize];
	node close[ElementSize];
	node path[ElementSize];
	int left_indicator = 0;
	int right_indicator = 0;
	bool exist = false;
	int close_indicator = 0;
	int size_of_open = 0;

	for (int i = 0; i < ElementSize; i++)//set all element of open and close to -1
	{
		open[i].num = -1;
		open[i].num_parent = -1;
		open[i].cost = 0;
		close[i].num = -1;
		close[i].num_parent = -1;
		close[i].cost = 0;
		path[i].num = -1;
		path[i].num_parent = -1;
		path[i].cost = 0;
	}
	H[0] = 366; H[1] = 0; H[2] = 160; H[3] = 242; H[4] = 161; H[5] = 77; H[6] = 151; H[7] = 226; H[8] = 244; H[9] = 241;
	H[10] = 234; H[11] = 380; H[12] = 98; H[13] = 193; H[14] = 253; H[15] = 329; H[16] = 80; H[17] = 199; H[18] = 178; H[19] = 374;

	for (int i = 0; i < ElementSize; i++)//set all element of W to 0
		for (int j = 0; j < ElementSize; j++)
			W[i][j] = 0;
	W[0][14] = 140; W[0][15] = 118; W[0][19] = 75; // set weight , Arad
	W[1][5] = 90; W[1][12] = 101; W[1][16] = 85; W[1][18] = 211;//Bucharest
	W[2][3] = 120; W[2][12] = 138; W[2][13] = 146;//Craiova
	W[3][2] = 120; W[3][9] = 75;//Dobreta
	W[4][6] = 86;//Eforie
	W[5][1] = 90;//Hiurgiu
	W[6][4] = 86; W[6][16] = 98;//Hirsova
	W[7][10] = 87; W[7][17] = 92;//Iasi
	W[8][9] = 70; W[8][15] = 111;//Lugoj
	W[9][3] = 75; W[9][8] = 70;//Mehadia
	W[10][7] = 87;//Neamt
	W[11][14] = 151; W[11][19] = 71;//Oradea
	W[12][1] = 101; W[12][2] = 138; W[12][13] = 97;//Pitesti
	W[13][2] = 146; W[13][12] = 97; W[13][14] = 80;//Rimnicu Vilcea
	W[14][0] = 140; W[14][11] = 151; W[14][13] = 80; W[14][18] = 99;//Sibiu
	W[15][0] = 118; W[15][8] = 111;//Timisoara
	W[16][1] = 85; W[16][6] = 98; W[16][17] = 142;//Urziceni
	W[17][7] = 92; W[17][16] = 142;//Vaslui
	W[18][1] = 211; W[18][14] = 99;//Yagaras
	W[19][0] = 75; W[19][11] = 71;//Zerind

	X.num = start;
	X.num_parent = -1;
	X.cost = 0;
	cout << "Start : " << city_name(start) << endl;

	while (X.num != goal)//loop until iterator until find goal
	{
		for (int i = 0; i < ElementSize; i++)//check there are child that is not found yet
			if (W[X.num][i] != 0)//check there are child of X.num
			{
				for (int j = 0; j < ElementSize; j++)//check child is in close
					if (i == close[j].num && (!(X.num == 12 && i == 1) && !(X.num == 18 && i == 1) && !(X.num == 5 && i == 1) && !(X.num == 16 && i == 1)))//except goal
						exist = true;
				for (int j = left_indicator; j < right_indicator; j++)//check child is in open
					if (i == open[j].num && (!(X.num == 12 && i == 1) && !(X.num == 18 && i == 1) && !(X.num == 5 && i == 1) && !(X.num == 16 && i == 1)))//except goal
						exist = true;
				if (!exist && (X.num != goal))//if it is not in close or open
				{
					open[right_indicator].num = i;
					open[right_indicator].num_parent = X.num;
					open[right_indicator].cost = X.cost + W[X.num][i];
					open[right_indicator].f = open[right_indicator].cost + H[i];
					right_indicator++;
					size_of_open++;
				}
				exist = false;
			}
		quicksort(open, left_indicator, size_of_open-1);
		if (open[left_indicator].num != -1)//take one element out
		{
			close[close_indicator] = X; // put X into close
			close_indicator++;
			X = open[left_indicator]; //take one element out
			left_indicator++;
		}
		cout << "selected node: " << city_name(X.num_parent) << " to " << city_name(X.num) << endl;
	}
	cout << endl << "now called is ( " << city_name(X.num_parent) << ", " << city_name(X.num) << ")" << endl;
	int k = 0;
	node A = X;
	while (A.num != start)//make path with parent
	{
		path[k++] = A;
		for (int j = 0; j < close_indicator; j++)//find backward
			if (A.num_parent == close[j].num)
			{
				A = close[j];
				break;
			}
	}
	path[k++] = A;//add starting point
	cout << "OPEN : [";
	for (int i = left_indicator; i < right_indicator; i++)//print open array
	{
		cout << " (" << city_name(open[i].num_parent) << " , " << city_name(open[i].num) << ") ";
	}
	cout << "]" << endl;

	cout << "Close : [";
	for (int i = 0; i < close_indicator; i++)//printf close array
	{
		cout << " (" << city_name(close[i].num_parent) << " , " << city_name(close[i].num) << ") ";
	}
	cout << "]" << endl;

	cout << "PATH :" << "[";
	for (int i = ElementSize - 1; i > -1; i--)//print path
		if (path[i].num != -1)
			cout << city_name(path[i].num) << "  ";
	cout << "]" << endl;

	cout << "Cost :" << "[";
	for (int i = ElementSize - 1; i > -1; i--)//print path
		if (path[i].num != -1)
			cout << path[i].cost << "  ";
	cout << "]" << endl;


	return 0;
}

