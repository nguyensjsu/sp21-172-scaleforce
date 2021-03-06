import { useEffect, useState } from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from 'react-router-dom';

import Dashboard from './screens/Dashboard';
import Login from './screens/Auth/Login';
import Signup from './screens/Auth/Signup';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Appointments from './screens/Appointments';
import Card from './screens/Card';
import { getCurrentUser } from './services/auth';

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isVerifying, setIsVerifying] = useState(true);

  useEffect(() => {
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
          path={[
            '/appointments?canceled=false',
            '/appointments?canceled=true',
            '/appointments',
          ]}
          component={Appointments}
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
        <Route
          exact
          path="/login"
          component={() =>
            isAuthenticated ? (
              <Redirect to="/dashboard" />
            ) : (
              <Login onLogin={() => setIsAuthenticated(true)} />
            )
          }
        />
        <Route
          exact
          path="/signup"
          component={Signup}
          onSignup={() => setIsAuthenticated(true)}
        />
        <Route
          exact
          path="/"
          component={() =>
            isAuthenticated ? (
              <Redirect to="/dashboard" />
            ) : (
              <Redirect to="/login" />
            )
          }
        />
      </Switch>
    </Router>
  );
}
