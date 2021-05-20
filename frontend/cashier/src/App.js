import { useState, useEffect } from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from 'react-router-dom';

import Login from './screens/Auth/Login';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Appointments from './screens/Appointments';
import NewAppointment from './screens/NewAppointment';
import { getCurrentUser } from './services/auth';

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
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
          path="/appointments"
          component={Appointments}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <ProtectedRoute
          exact
          path="/newappointment"
          component={NewAppointment}
          isAuthenticated={isAuthenticated}
          isVerifying={isVerifying}
        />
        <Route
          path="/login"
          component={() =>
            isAuthenticated ? (
              <Redirect to="/appointments" />
            ) : (
              <Login onLogin={() => setIsAuthenticated(true)} />
            )
          }
        />
        <Route path="/" component={isAuthenticated ? Appointments : Login} />
      </Switch>
    </Router>
  );
}
