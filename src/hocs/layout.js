import React from 'react';
import Head from 'next/head';
import { Container, Typography } from '@material-ui/core';

export default function({ children }) {
  return (
    <div>
      <Head>
        <title>SendTracker</title>
        <meta charSet='utf-8' />
        <meta name='viewport' content='initial-scale=1.0, width=device-width' />
      </Head>
      
      <header>
        <nav>
          {/* Future links go here */}
        </nav>
      </header>

      {children}

      <footer>
        <Container maxWidth="sm">
          <Typography variant="caption" align="center">
            Nicolas Pennie &copy; 2019
          </Typography>
        </Container>
      </footer>
    </div>
  );  
}
