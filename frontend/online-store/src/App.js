import React, {useEffect} from 'react';
import { useState } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Navbar from './components/Navbar'
import Signup from './screens/Auth/Signup'
import Login from './screens/Auth/Login'
import Dashboard from "./screens/Dashboard";
import Users from "./screens/Users";
import ProtectedRoute from "./components/ProtectedRoute";
import Appointments from "./screens/Appointments";
import Calendar from "./screens/Calendar";
import {getCurrentUser} from "./services/auth";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [isVerifying, setIsVerifying] = useState(false);

  useEffect( () => {
    setIsVerifying(true);
    const user = getCurrentUser();
    setIsAuthenticated(!!user);
    setIsVerifying(false);
  }, [isAuthenticated]);

  return (
    <Router>
      {isAuthenticated && <Navbar onLogout={() => setIsAuthenticated(false)} />}
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
          path="/users"
          component={Users}
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
        <Route
          path="/login"
          component={() =>
            isAuthenticated ? (
              <Redirect to="/"
            )
          }
        />
        <Route path="/signup" component={Signup} />
        <Route path="/" component={isAuthenticated ? Dashboard : Login} />
      </Switch>
    </Router>
  );
}
