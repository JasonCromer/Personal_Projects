#include <iostream>
#include <string>
#include <cstdlib>
#include <cmath>

using namespace std;





int main() 
{
int *testScores;
int size;
double sum = 0;
double averageScore = 0;

 cout << "Enter test Scores" << endl;
 cin >> size;

 testScores = new int[size];

 for(int i = 0; i < size; i++)
 {
	 cout << "Enter score number " << (i+1) << ": ";
	 cin >> testScores[i];
 }

 for(int z = 0; z < size; z++)
 {
	 sum += testScores[z];
 }
 
 averageScore = sum/size;
 cout << averageScore;

}