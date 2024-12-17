import React from "react";
import { List, ListItem, ListItemText } from "@mui/material";
import { NavLink } from "react-router-dom";

const Navbar = () => {
  return (
    <List>
      <ListItem button component={NavLink} to="/materials">
        <ListItemText primary="原物料管理" />
      </ListItem>
      <ListItem button component={NavLink} to="/vendors">
        <ListItemText primary="廠商管理" />
      </ListItem>
      <ListItem button component={NavLink} to="/sites">
        <ListItemText primary="工地管理" />
      </ListItem>
    </List>
  );
};

export default Navbar;
