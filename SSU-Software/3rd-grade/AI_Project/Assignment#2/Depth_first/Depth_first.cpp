// Depth_first.cpp : 콘솔 응용 프로그램에 대한 진입점을 정의합니다.

#include "stdafx.h"

#include<iostream>

#define ElementSize 20
using namespace std;

typedef struct node {
	int num_parent;
	int num;
}node;

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

	node open[ElementSize * 3];//need to take out list
	int left_indicator = ElementSize * 3 - 1;
	int right_indicator = ElementSize * 3-1;//point end of open
	node close[ElementSize*3];//no need to take out list
	int close_indicator = 0;
	bool end = false;
	int cost = 0;
	int start = 15;//Timisoara
	int goal = 1;//Bucharest
	node X; // iterate
	bool exist = false;//check it is in close
	int path[ElementSize];

	for (int i = 0; i < ElementSize*3; i++)//set all element of open and close to -1
	{
		open[i].num = -1;
		open[i].num_parent = -1;
		close[i].num = -1;
		close[i].num_parent = -1;
	}

	for (int i = 0; i < ElementSize; i++)//set all element of W to 0
		for (int j = 0; j < ElementSize; j++)
			W[i][j] = 0;
	W[0][14] = 140; W[0][15] = 118; W[0][19] = 75; // set weight , Arad
	W[1][5] = 90; W[1][12] = 101; W[1][16] = 85; W[1][18] = 211;//Bucharest
	W[2][3] = 120; W[2][12] = 138; W[2][13] = 146;//Craiova
	W[3][2] = 120; W[3][9] = 75;//Dobreta
	W[4][6] = 86;//Eforie
	W[5][1] = 90;//Giurgiu
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
	cout << "Start : " << city_name(start) << endl;

	while (!end)//loop until open is empty
	{
		for (int i = ElementSize-1; i >-1; i--)//check there are child that is not found yet
			if (W[X.num][i] != 0)//check there are child of X.num
			{
				for (int j = 0; j < ElementSize; j++)//check child is in close
					if (i == close[j].num && (!(X.num == 12 && i == 1) && !(X.num == 18 && i == 1) && !(X.num == 16 && i == 1) && !(X.num == 5 && i == 1)))//except goal
						exist = true;
				for (int j = left_indicator; j <= right_indicator; j++)//check child is in open
					if (i == open[j].num && (!(X.num == 12 && i == 1) && !(X.num == 18 && i == 1) && !(X.num == 16 && i == 1) && !(X.num == 5 && i == 1)))//except goal
						exist = true;
				if (!exist && (X.num != goal))//if it is not in close or open
				{
					left_indicator--;
					open[left_indicator].num = i;
					open[left_indicator].num_parent = X.num;	
				}
				exist = false;
			}
		if (open[left_indicator].num != -1)//take one element out
		{
			close[close_indicator] = X; // put X into close
			close_indicator++;
			X = open[left_indicator]; //take one element out
			left_indicator++;
		}
		cout << "selected node: " << city_name(X.num_parent) << " to " << city_name(X.num) << endl;
		if (X.num == goal) {
			cout << endl<<"now called is ( " << city_name(X.num_parent) << ", " << city_name(X.num) << ")" << endl;

			for (int i = 0; i < ElementSize; i++)//set all element of path to -1
				path[i] = -1;

			int k = 0;
			node A = X;
			while (A.num != start)//make path with parent
			{
				path[k++] = A.num;
				cost += W[A.num_parent][A.num];
				for (int j = 0; j < close_indicator; j++)//find backward
					if (A.num_parent == close[j].num)
					{
						A = close[j];
						break;
					}
			}
			path[k++] = A.num;//add starting point

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
				if (path[i] != -1)
					cout << city_name(path[i]) << "  ";
			cout << "]" << endl;
			cout << "cost : " << cost << endl << endl;//print cost;
			cost = 0;
		}
		if (left_indicator == right_indicator)//check open is empty
			end = true;
		else
			end = false;
	}
	
	return 0;
}


