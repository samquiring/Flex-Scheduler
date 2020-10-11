# Flex-Scheduler
 A program that is intended to help high/middle schools schedule certain students and teachers on certain days. It was orignally named Simple Scheduler but has now been changed to Flex Scheduler in an effort to increase readability, as that is now the colloquial name for such a program.
 
 This program was developed by Sam Quiring and was originally marketed by Brendan Tran and Ian Slater but has since been retired and now made public for anyone to use because multiple large school scheduling companies have implemented a similar idea into their programs making the need to pay for this specific program now obsolete. 
 
 What it does:
  It takes a csv file of a current schedule of students including: their classes, what period the class is in, and who is teaching the class at a given school. It then outputs a csv file that splits all students at the school into different groups depending on what day they will go into class on. It can split the school into 2-5 different groups that have roughly the same number of students in each group. Each group would be attending class on different days to allow for better social distancing in high schools while also enabling students to get an in person education. These groups are created from attempting to get every student in one group and every class to have roughly the same amount of students on any given day. 
 
 How I did it:
  This program was the work of roughly two months of planning and then programming to allow for as module of a program as possible to meet the needs for as many schools as possible. Much of this is seen and described directly in the code so I will only focus on the main algorithm. The program takes in how many groups the school wants, what is the largest range they are willing to allow from one group to another, and all of the students and classes which is analysed by a custom built csvReader. It then puts all the students into group 1 to start.
 
1. Checks if all groups are in range. 
 If they are then program is done!
 If not it finds the group that is the furthest from the specified center, which is 1/(number of groups). Let's call this group A

2. Checks if group A is Within a given range. 
 If it is then goes back to step 1.
 If not it checks within group A to find the Class that is furthest from the center. Let's call this Class A

3. Checks if Class A is within the range.
 If it is then goes back to step 2
 If it is not then within Class A it goes to a random student and changes its group to group A and calculates based off my algorithm if the change to A was a good      change.

4. If it was not a good change it reverts that student back to its previous group.
   It then goes back to step 3.

5. If it is not possible to get a given class into the range then it widens the range and checks if the new range is smaller or equal to the largest range the school is willing to allow. 
 If it is not it turns off a preference like what group students prefer to be in. 
   If there are no more preferences it throws an error telling the user that their range is too small and they must widen it. 
It then runs #1 agian with a widened range. 
   
   
   
