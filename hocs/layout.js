import React from 'react';
import Link from 'next/link'
import Head from 'next/head'

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

      <footer>Nicolas Pennie &copy; 2019</footer>
    </div>
  );  
}
