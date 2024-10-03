# Parallel and Concurrent Programming Projects

This repository contains source code for two parallel and concurrent programming projects. Each project addresses different aspects of multi-threaded programming and performance optimization.

## Table of Contents
- [PCP1: Parallel Abelian Sandpile Automation](#pcp1-parallel-abelian-sandpile-automation)
  - [Project Overview](#project-overview)
  - [Compilation and Execution](#compilation-and-execution)
  - [Report](#report)
- [PCP2: Concurrent Swimming Medley Relay Simulation](#pcp2-concurrent-swimming-medley-relay-simulation)
  - [Project Overview](#project-overview-1)
  - [Compilation and Execution](#compilation-and-execution-1)
  - [Report](#report-1)

---

## PCP1: Parallel Abelian Sandpile Automation

### Project Overview
PCP1 focuses on optimizing a serial Abelian sandpile automation program by producing a parallel solution. The original program simulates the dynamics of a sandpile and applies rules for sand redistribution. The parallelization introduces concurrency to enhance performance and reduce the time required to reach a stable configuration.

**Key goals include:**
- Implementing a grid-based parallel decomposition using the Fork/Join framework.
- Balancing load distribution across threads.
- Avoiding race conditions and synchronization issues in grid updates.

### Compilation and Execution
To compile and run the project, use the following commands:

1. **Compilation:**
   ```bash
   make
2. **Execution:**
   java -cp bin parallelAbelianSandpile.AutomatonSimulation input/<input_file.csv> output/<output_file.png>

**Report**
A detailed report (ZKXZOL001_PCP1_Report.pdf) is available in the PCP1/ directory. The report provides feedback on the parallelization strategies employed, performance analysis, and solutions to challenges encountered during development.





# PCP2: Concurrent Swimming Medley Relay Simulation

## Project Overview
PCP2 focuses on introducing thread safety and synchronization into a concurrent swimming medley relay simulation. The project simulates multiple teams of swimmers participating in a relay race, where proper thread coordination is essential to ensure the correct sequence of events for each team and swimmer.

**Key objectives include:**
- Implementing proper thread synchronization using Java’s `synchronized` keyword, locks, and conditions.
- Ensuring no data corruption or race conditions occur during the race.
- Using thread signaling to efficiently coordinate swimmer transitions.

## Compilation and Execution
To compile and run the project, follow these steps:

1. **Compilation:**
   ```bash
   make

2. **Execution:**
   java -cp bin concurrentSwimmingRelay.SwimmingRelaySimulation


**Report**
A detailed report (ZKXZOL001_PCP2_Report.pdf) is available in the reports/ directory. The report discusses:

- The thread safety mechanisms employed in the simulation.
- Performance considerations for the concurrency model used.
- Reflections on the challenges and solutions implemented during development.
Contributions
Feel free to open a pull request or an issue if you have any suggestions or improvements!













