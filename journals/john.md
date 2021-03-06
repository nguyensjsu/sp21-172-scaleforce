# John's Journal

## Week 1: 4/15 - 4/22

### Tasks

- [ ] Created the cashier app frontend boilerplate project.
- [ ] Pair programmed with Jesus on frontend tools selections and implementations.
- [ ] Wrote out the requirements and data models needed for our application.

### Accomplishments

1. https://github.com/nguyensjsu/sp21-172-scaleforce/commit/358b52210ed3e899341e4f8c7d52cafd2c5d0771
2. https://docs.google.com/document/d/1v6vR-BztWN1lZUez-DuX5xZQt1WL9oXsM2LmtTgV17w/edit?usp=sharing

### Challenges

- When implementing tailwindcss some styles were not being used correctly. After looking further into this it turns out we needed to add more modules to show new features

## Week 2: 4/22 - 4/29

### Tasks

- [ ] Setup backdoor dashboard
- [ ] Created nav bar
- [ ] Create User, Appointments, Sales View

### Accomplishments

[Branch of work](https://github.com/nguyensjsu/sp21-172-scaleforce/tree/week-2)
I was able to get this working in this branch. This week was cut short due to other assignments. But I was able to build and test authenticated routing with react-router. That will be implemented tomorrow ideally!

### Challenges

A challenged I faced was figuring out how we are going to work with hitting our proxy server. Lucky @patrick was able to point us in the right direction with a configutaion we can run!

## Week 3: 4/29 - 5/6

### Tasks

![john week 3](./images/john/jg-1.png)

- [x] add protected routes to the frontend of backoffice (will be copy pasted for the other apps)
- [x] created the views and tables for the back office (can and will be reused in the other apps)
- [x] started working with the auth server to add auth functionality to frontned

### Accomplishments

[Added protected routes to react router](https://github.com/nguyensjsu/sp21-172-scaleforce/pull/27)
[Added backoffice views](https://github.com/nguyensjsu/sp21-172-scaleforce/pull/27)

### Challenges

I didn't face any challenges this week besides a very busy schedule :P

## Week 4: 5/6 - 5/13

### Tasks

![john week 4](./images/john/jg-2.png)

- [x] i worked on syncing the backend auth server with react.
- [x] deployed the backend office app on Netlify
- [x] created frontend services

### Accomplishments

[work for auth](https://github.com/nguyensjsu/sp21-172-scaleforce/tree/add-auth-to-frontend)
[link to netlify](https://vigilant-wiles-bee8f3.netlify.app/)

### Challenges

the auth server was frustrating to work with because of a cors issue. this happens when you don't have a proxy or are querying from a non trusted source. we are going to fix that by ignoring cors to avoid setting up the proxy for now
