# README

# 🧩 Minecraft Epidemic Simulation (Java Server Plugin)

A multiplayer game-based simulation built on Minecraft to study how players adapt their behaviour and decision-making during a disease outbreak.

---

## 📌 Overview

This project implements a custom Minecraft server plugin that simulates the spread of an infectious disease in a dynamic multiplayer environment.

Unlike traditional epidemiological models with predefined rules, this system allows players to:

- Experience real-time epidemic progression
- Adapt their behaviour based on risk perception
- Make vaccination decisions influenced by social and network effects

The goal is to explore how individuals learn and respond to uncertainty in an interactive setting.

---

## 🛠️ Features

- **Custom Minecraft server plugin (Java)**
- **Event-driven simulation of disease spread**
- Player-to-player interaction tracking
- Dynamic infection and recovery logic
- Vaccination decision system
- Behavioural data collection (movement, exposure, interactions)

---

## 🎮 Simulation Design

- Multiplayer environment with **18 participants**
- Simulation runs over **34 in-game days**
- Players interact in a shared world with evolving infection risk
- Disease spreads through proximity and interaction events

### Key Mechanics

- Infection probability based on player contact
- Vaccination decisions made by players in real time
- Risk perception evolves based on:
    - personal exposure
    - observed infections
    - network connections

---

## 🏗️ Technical Details

- **Language:** Java
- **Platform:** Minecraft Server (Plugin-based architecture)
- **Architecture:** Event-driven system using server-side hooks
- **Core Components:**
    - Infection logic engine
    - Player interaction tracking
    - State management (infected / recovered / vaccinated)
    - Data logging for analysis

---

## 📄 Publication

This project contributed to the following research:

> *Multiplayer Online Game-Based Framework for Exploring Human Contact Behavior and Adaptive Decision-Making During Pandemics*
> 
> 
> ACM Conference (2026)
> 

---

## 📊 Key Findings

- Players progressively adapted their behaviour over time
- **Early adopters** of vaccination relied on individual risk perception
- **Late adopters** were influenced by social observation and epidemic trends
- Highly connected players tended to vaccinate earlier
- Some players exhibited **free-riding behaviour**, delaying vaccination when surrounded by immunised peers
- Movement patterns and risk-taking behaviour changed as players gained experience

---

## 🧠 Research Contributions

This project demonstrates how game-based environments can be used to study:

- Adaptive decision-making under uncertainty
- Social and network effects in epidemic response
- Behavioural changes in interactive simulations

It contributes to:

- Game-based learning
- Computational epidemiology
- Public health education