// @flow

import React from 'react'
import { Button, Navbar } from '@blueprintjs/core'
import { Alignment } from '@blueprintjs/core/lib/cjs/common/alignment'
import { Link } from '@reach/router'

class Header extends React.Component<{}> {
  render () {
    return <Navbar>
      <Navbar.Group align={Alignment.LEFT}>
        <Navbar.Heading>Workflix</Navbar.Heading>
        <Navbar.Divider/>
        <Link to='/'><Button className='bp3-minimal' icon='layers' text='Tasks overview'/></Link>
        <Link to='process-templates'><Button className='bp3-minimal' icon='new-layers' text='Process templates'/></Link>
        <Link to='users'><Button className='bp3-minimal' icon='user' text='Users'/></Link>
      </Navbar.Group>
    </Navbar>
  }
}

export default Header
