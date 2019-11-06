import React from 'react'
import { Container, Typography } from '@material-ui/core';
import Layout from '../src/hocs/layout';

const Title = () => {
  return (
    <Container maxWidth="sm">
      <Typography variant="h3" align="center" gutterBottom>
        Welcome to SendTracker
      </Typography>
    </Container>
  );
}

const Footer = () => {
  return (
    <Typography variant="caption" align="center">
      Nicolas Pennie &copy; 2019
    </Typography>
  );
}

export default function() {
  return (  
    <Layout
      title={ <Title/> }
      footer={ <Footer/> }
    />
  )
}

