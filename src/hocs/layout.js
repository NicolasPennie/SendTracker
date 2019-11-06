import React from 'react';
import Head from 'next/head';
import styled from 'styled-components';

const AppContainer = styled.div`
  height: 100vh;
  width: 100vw;
  display: grid;
  grid-template-columns: 20em auto 20em;
  grid-template-rows: 10% auto 5%;
`

const Title = styled.div`
  grid-column-start: 2;
  grid-row-start: 1;
  margin: 2em;
`

const Content = styled.div`
  grid-column-start: 2;
  grid-row-start: 2;
  border: 0.5em solid;
  border-radius: 1em;
`

const Footer = styled.div`
  grid-column-start: 2;
  grid-row-start: 3;
  margin: 1em;
`

export default function({ title, content, footer }) {
  return (
    <AppContainer>
      <Head>
        <title>SendTracker</title>
        <meta charSet='utf-8' />
        <meta name='viewport' content='initial-scale=1.0, width=device-width' />
      </Head>

      <Title>
        {title}
      </Title>

      <Content>
        {content}
      </Content>

      <Footer>
        {footer}
      </Footer>
    </AppContainer>
  );  
}
