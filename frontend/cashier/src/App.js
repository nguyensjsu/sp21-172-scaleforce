import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Home from './screens/Home/Home';
import Login from './screens/Auth/Login';
import Navbar from './components/Navbar'

export default function App() {
  return (
    <Router>
      <Navbar/>
      <Switch>
        <Route path="/login">
          <Login />
        </Route>
        <Route path="/">
          <Home />
        </Route>
      </Switch>
    </Router>
  );
}
