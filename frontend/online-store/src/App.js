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
import { getCurrentUser } from './services/auth';

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [isVerifying, setIsVerifying] = useState(false);

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
          path="/appointments"
          component={Appointments}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <Route
          path="/login"
          component={() =>
            isAuthenticated ? (
              <Redirect to="/dashboard" />
            ) : (
              <Login onLogin={() => setIsAuthenticated(true)} />
            )
          }
        />
        <Route path="/signup" component={Signup} />
        <Route
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
