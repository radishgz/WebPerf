/*
 * SonarQube Web Plugin
 * Copyright (C) 2010 SonarSource and Matthijs Galesloot
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonar.plugins.web.visitor;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import org.sonar.plugins.web.node.CommentNode;
import org.sonar.plugins.web.node.DirectiveNode;
import org.sonar.plugins.web.node.ExpressionNode;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.node.TagNode;
import org.sonar.plugins.web.node.TextNode;


import java.nio.charset.Charset;
import java.util.List;

/**
 * Scans the nodes of a page and send events to the visitors.
 *
 * @author Matthijs Galesloot
 * @since 1.0
 */
public class HtmlAstScanner {

  private static final Logger logger = LoggerFactory.getLogger(HtmlAstScanner.class);
	
  private final List<DefaultNodeVisitor> metricVisitors;
  private final List<DefaultNodeVisitor> checkVisitors = Lists.newArrayList();
  Project project;

  public HtmlAstScanner(Project project2, List<DefaultNodeVisitor> metricVisitors) {
    this.metricVisitors = metricVisitors;
    project=project2;
  }

  /**
   * Add a visitor to the list of visitors.
   */
  public void addVisitor(DefaultNodeVisitor visitor) {
	  //set project info
	  visitor.setProject(project);
    checkVisitors.add(visitor);
    
  }

  /**
   * Scan a list of Nodes and send events to the visitors.
   */
  public void scan(List<Node> nodeList, WebSourceCode webSourceCode, Charset charset) {
    scan(nodeList, webSourceCode, charset, metricVisitors);
    scan(nodeList, webSourceCode, charset, checkVisitors);
  }

  private void scan(List<Node> nodeList, WebSourceCode webSourceCode,
	Charset charset, List<DefaultNodeVisitor> visitors) {
    // prepare the visitors
    for (DefaultNodeVisitor visitor : visitors) {
      visitor.setSourceCode(webSourceCode);
    
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(charset);
      }
    }

    // notify visitors for a new document
    for (DefaultNodeVisitor visitor : visitors) {
      //modify 
      try{
    	  visitor.startDocument(nodeList);
      }catch (Exception e){
    	  logger.error("Exception occured when revoke startDocument " + visitor.getClass().toString(), e);      
    	  }
    }

    // notify the visitors for start and end of element
    for (Node node : nodeList) {
      for (DefaultNodeVisitor visitor : visitors) {
    	  try{
    		  scanElement(visitor, node);
      }catch (Exception e){
    	  logger.error("Exception occured when revoke scanElement " + visitor.getClass().toString(), e);      
      }
      }
    }

    // notify visitors for end of document
    for (DefaultNodeVisitor visitor : visitors) {
    	try{
    	      visitor.endDocument();

    	}catch (Exception e){
      	  logger.error("Exception occured when revoke endDocument " + visitor.getClass().toString(), e);      
      	  
        }
    }
  
  }

  /**
   * Scan a single element and send appropriate event: start element, end element, characters, comment, expression or directive.
   */
  private void scanElement(DefaultNodeVisitor visitor, Node node) {
    switch (node.getNodeType()) {
      case TAG:
        TagNode element = (TagNode) node;
        if (!element.isEndElement()) {
          visitor.startElement(element);
        }
        if (element.isEndElement() || element.hasEnd()) {
          visitor.endElement(element);
        }
        break;
      case TEXT:
        visitor.characters((TextNode) node);
        break;
      case COMMENT:
        visitor.comment((CommentNode) node);
        break;
      case EXPRESSION:
        visitor.expression((ExpressionNode) node);
        break;
      case DIRECTIVE:
        visitor.directive((DirectiveNode) node);
        break;
      default:
        break;
    }
  }

}
