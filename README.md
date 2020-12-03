# AntColonies
Objects Ant (Ant), Cell (Point in an ant field) and CellGrid (Cartesian ant field) are tested in the AntColonies.java program which simulates, for a specific state, the behavior of ants in searching and transporting food to their nests.

## Details
Ants live and move in a Cartesian field N x N for a given N. Each nest is at a specific point, and each ant belongs to one of the nests. In addition, in certain places (where there are no nests) there are quantities of food that are measured in number of seeds. Each ant acts independently of the other ants whether they belong to its nest or another nest, and there is no direct communication between them. All ants move individually, step by step (at each point in time). The common goal of all ants is to search for food, so that their nests end up having as many seeds as possible. Each nest can hold an unlimited number of seeds but an ant can carry a seed at any time. In addition, the ants memory is very limited. In particular, it knows only the location of its nest, and it also remembers the immediately preceding location from which it was led to the present location, which it also knows. Finally, it can only move horizontally or vertically from where it is located, so each movement is a step up, down, right or left.
