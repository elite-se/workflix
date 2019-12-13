// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import type { ProcessTemplateMasterDataType } from './models'
import withPromiseResolver from './withPromiseResolver'
import { Link } from '@reach/router'

class ProcessTemplates extends React.Component<{ templates: Array<ProcessTemplateMasterDataType> }> {
  render () {
    return <div style={{
      margin: '20px',
      display: 'flex',
      maxWidth: '300px',
      flex: '1',
      justifyContent: 'flex-start',
      alignItems: 'stretch',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>All Process Templates</H2>
      {
        this.props.templates.map(template => (
          <Link to={`./${template.id}`} style={{ margin: '5px' }} key={template.id}><Card>
            <H3>{template.title}</H3>
            <Text>Erstellt von: <i>{template.owner.name} ({template.owner.displayname})</i></Text>
          </Card></Link>)
        )
      }
    </div>
  }
}

const mockTemplates: Array<ProcessTemplateMasterDataType> = [
  {
    id: 0,
    title: 'Some Process',
    durationLimit: 20,
    owner: {
      displayname: 'MM',
      email: 'markl@integreat-app.de',
      id: 1,
      name: 'Michael Markl'
    }
  },
  {
    id: 0,
    title: 'Some Process',
    durationLimit: 20,
    owner: {
      displayname: 'MM',
      email: 'markl@integreat-app.de',
      id: 1,
      name: 'Michael Markl'
    }
  }
]

export default withPromiseResolver(() => Promise.resolve({ templates: mockTemplates }))(
  ProcessTemplates
)
