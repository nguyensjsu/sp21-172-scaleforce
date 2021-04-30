import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import Navbar from './components/Navbar'
import Signup from './screens/Auth/Signup'
import Login from './screens/Auth/Login'
import Home from "./screens/Home";

export default function App() {
  return (
    <Router>
      <Navbar/>
      <Switch>
        <Route path="/signup">
          <Signup/>
        </Route>
        <Route path="/login">
          <Login/>
        </Route>
        <Route path="/">
          <Home/>
        </Route>
      </Switch>
    </Router>
  );
}