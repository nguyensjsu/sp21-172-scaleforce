import { useState } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Login from './screens/Auth/Login';
import Navbar from './components/Navbar'
import ProtectedRoute from "./components/ProtectedRoute";
import Appointments from "./screens/Appointments";
import Signup from "./screens/Auth/Signup";
import NewAppointment from "./screens/NewAppointment";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [isVerifying,setIsVerifying] = useState(false);
  return (
    <Router>
      {isAuthenticated && <Navbar/>}
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
        <Route path="/login" component={Login} />
        <Route path="/signup" component={Signup} />
        <Route path="/" component={isAuthenticated ? Appointments : Login} />
      </Switch>
    </Router>
  );
}
