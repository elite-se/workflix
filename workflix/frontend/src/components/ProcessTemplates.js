// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import type { ProcessTemplateMasterDataType } from '../datatypes/models'
import withPromiseResolver from './withPromiseResolver'
import { Link } from '@reach/router'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  margin: 5px;
`

type PropsType = { templates: Array<ProcessTemplateMasterDataType> }

class ProcessTemplates extends React.Component<PropsType> {
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
          <CustomLink to={`./${template.id}`} key={template.id}><Card>
            <H3>{template.title}</H3>
            <Text>Erstellt von: <i>{template.owner.name} ({template.owner.displayname})</i></Text>
          </Card></CustomLink>)
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
      id: '1',
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
      id: '1',
      name: 'Michael Markl'
    }
  }
]

export default withPromiseResolver<PropsType, {| templates: Array<ProcessTemplateMasterDataType> |}>(
  () => Promise.resolve({ templates: mockTemplates })
)(ProcessTemplates)
