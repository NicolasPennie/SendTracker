import React from 'react'
import { Container, Typography } from '@material-ui/core';
import Layout from '../src/hocs/layout';

export default function() {
  return (  
    <Layout>
      <Container maxWidth="sm">
        <Typography variant="h3" align="center" gutterBottom>
          Welcome to SendTracker
        </Typography>
      </Container>
    </Layout>
  )
}

