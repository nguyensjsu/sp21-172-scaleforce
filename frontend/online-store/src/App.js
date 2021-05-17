import React from 'react';
import { useState } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Navbar from './components/Navbar'
import Signup from './screens/Auth/Signup'
import Login from './screens/Auth/Login'
import Dashboard from "./screens/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import Appointments from "./screens/Appointments";
import Calendar from "./screens/Calendar";
import Card from "./screens/Card";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [isVerifying, setIsVerifying] = useState(false);

  return (
    <Router>
      {isAuthenticated && <Navbar />}
      <Switch>
        <ProtectedRoute
          exact
          path="/dashboard"
          component={Dashboard}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <ProtectedRoute
          exact
          path="/appointments"
          component={Appointments}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <ProtectedRoute
          exact
          path="/calendar"
          component={Calendar}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <ProtectedRoute
          exact
          path="/card"
          component={Card}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <Route path="/login" component={Login} />
        <Route path="/signup" component={Signup} />
        <Route path="/" component={isAuthenticated ? Dashboard : Login} />
      </Switch>
    </Router>
  );
}
